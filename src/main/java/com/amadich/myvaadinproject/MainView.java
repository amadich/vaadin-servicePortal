package com.amadich.myvaadinproject;

import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.avatar.Avatar;
import com.amadich.myvaadinproject.backend.model.DeployRequest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;
import java.util.Random;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
@Route("")
@PageTitle("DevOps Self Service Portal")
public class MainView extends VerticalLayout {

    public MainView() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);


        H1 title = new H1("INBOX Hosting - Self Service Portal");
        title.getStyle().set("color", "darkblue");
ProgressBar progressbar = new ProgressBar();
progressbar.setValue(0.75);

        Avatar avatar = new Avatar("","https://dash.inbox.com.iq/_next/image?url=%2F_next%2Fstatic%2Fmedia%2Finbox_main_fix_network.8cf7b05c.png&w=1200&q=75");
        avatar.setHeight("96px");
        avatar.setWidth("96px");

        TextField projectName = new TextField("Project Name");
        projectName.setWidth("300px");


        Grid<ServiceType> grid = new Grid<>(ServiceType.class, false);

        grid.addComponentColumn(service -> {
            Image img = new Image("/images/" + service.getImage(), "");
            img.setHeight("40px");
            return img;
        }).setHeader("Service");

        grid.addColumn(ServiceType::getName)
                .setHeader("Name");

        grid.addColumn(ServiceType::getDockerImage)
                .setHeader("Docker Image");

        grid.addColumn(ServiceType::getDescription)
                .setHeader("Description");

        grid.addComponentColumn(service -> {

            Button select = new Button("Configure");

            select.addClickListener(e ->
                    showServiceForm(service, projectName.getValue())
            );

            return select;

        }).setHeader("Action");

        grid.setItems(ServiceType.values());
        grid.setWidth("900px");
        grid.getStyle().set("margin", "0 auto");
add(title, progressbar, avatar, projectName, grid);

    }

    private void showServiceForm(ServiceType service, String project) {

        Dialog dialog = new Dialog();


        dialog.setWidth("650px");
        dialog.getElement().getStyle().set("margin-left", "0");
        dialog.getElement().getStyle().set("margin-right", "auto");

        VerticalLayout layout = new VerticalLayout();

        H3 title = new H3("Configure " + service.getName());

        Image image = new Image("/images/" + service.getImage(), "");
        image.setHeight("60px");

        Span docker = new Span("Image: " + service.getDockerImage());

        FormLayout form = new FormLayout();

        TextField projectField = new TextField("Project Name");
        projectField.setValue(project);

        ComboBox<Integer> cpu = new ComboBox<>("CPU");
        cpu.setItems(1, 2, 4);

        ComboBox<String> ram = new ComboBox<>("RAM");
        ram.setItems("1GB", "2GB", "4GB");

        ComboBox<String> storage = new ComboBox<>("Storage");
        storage.setItems("10GB", "20GB", "50GB");

        form.add(projectField);

        switch (service){

            case POSTGRES -> {

                TextField user = new TextField("Username");

                PasswordField pass = new PasswordField("Password");

                TextField db = new TextField("Database");

                Button gen = new Button("Generate");

                gen.addClickListener(e -> db.setValue(generatePsqlDB()));

                form.add(user, pass, db, gen);
            }

            case MYSQL -> {

                PasswordField root = new PasswordField("Root Password");

                TextField db = new TextField("Database");

                Button gen = new Button("Generate");

                gen.addClickListener(e -> db.setValue(generateMysqlDB()));

                form.add(root, db, gen);
            }

            case REDIS -> {

                PasswordField pass = new PasswordField("Password");

                form.add(pass);
            }

            case RABBITMQ -> {

                TextField user = new TextField("Username");

                PasswordField pass = new PasswordField("Password");

                TextField vhost = new TextField("Virtual Host");

                form.add(user, pass, vhost);
            }
        }

        form.add(cpu, ram, storage);

        Button deploy = new Button("Deploy Service");
        Button cancel = new Button("Cancel");

        deploy.addClickListener(e -> {

            deploy.setEnabled(false);

            RestTemplate restTemplate = new RestTemplate();

            DeployRequest request = new DeployRequest();

            request.setProjectName(projectField.getValue());
            request.setService(service.name().toLowerCase());

            String url = "http://localhost:8080/api/deploy";

            try {

                restTemplate.postForObject(url, request, String.class);

                Notification.show(
                        service.getName() + " deployed successfully",
                        3000,
                        Notification.Position.MIDDLE
                );

                dialog.close();

            } catch (Exception ex) {

                Notification.show("Deployment failed");

                deploy.setEnabled(true);
            }
        });

        cancel.addClickListener(e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(cancel, deploy);

        layout.add(title, image, docker, form, buttons);

        dialog.add(layout);

        dialog.open();
    }

    private String generatePsqlDB() {

        Random r = new Random();
        int n = r.nextInt(9000) + 1000;

        return "pq-" + n;
    }

    private String generateMysqlDB() {

        Random r = new Random();
        int n = r.nextInt(9000) + 1000;

        return "mysql-" + n;
    }
}
