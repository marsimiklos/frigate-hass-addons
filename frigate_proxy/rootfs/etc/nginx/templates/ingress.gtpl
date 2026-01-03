server {
    listen 5000 default_server;

    include /etc/nginx/includes/server_params.conf;

    location / {
        allow   172.30.32.2;
        deny    all;

        proxy_pass {{ .server }};
        proxy_set_header X-Ingress-Path {{ .entry }};

        {{ if .auth_secret }}
        proxy_set_header X-Proxy-Secret "{{ .auth_secret }}";
        {{ end }}

        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header Authorization "";

        {{ if .proxy_pass_host }}
          proxy_set_header Host $http_host;
        {{ end }}
        {{ if .proxy_pass_real_ip }}
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header X-Real-IP $remote_addr;
        {{ end }}

        include /etc/nginx/includes/proxy_params.conf;
    }
}