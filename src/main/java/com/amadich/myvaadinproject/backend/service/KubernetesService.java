package com.amadich.myvaadinproject.backend.service;

import com.amadich.myvaadinproject.backend.model.DeployRequest;
import com.amadich.myvaadinproject.backend.util.ShellExecutor;
import org.springframework.stereotype.Service;

@Service
public class KubernetesService {

    public void deployService(DeployRequest request) {

        String namespace = request.getProjectName();

        createNamespaceIfNotExists(namespace);

        switch (request.getService()) {

            case "postgres":
                installPostgres(namespace, request);
                break;

            case "redis":
                installRedis(namespace);
                break;
        }
    }

    private void createNamespaceIfNotExists(String namespace) {

        String cmd = "kubectl get namespace " + namespace +
                " || kubectl create namespace " + namespace;

        ShellExecutor.run(cmd);
    }

    private void installPostgres(String namespace, DeployRequest request) {

        String cmd =
                "helm install postgres-" + namespace +
                        " bitnami/postgresql " +
                        "--namespace " + namespace +
                        " --set auth.username=" + request.getUsername() +
                        " --set auth.password=" + request.getPassword() +
                        " --set auth.database=" + request.getDatabase();

        ShellExecutor.run(cmd);
    }

    private void installRedis(String namespace) {

        String cmd =
                "helm install redis-" + namespace +
                        " bitnami/redis " +
                        "--namespace " + namespace;

        ShellExecutor.run(cmd);
    }
}
