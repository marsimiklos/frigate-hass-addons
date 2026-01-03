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

        # A proxy_params.conf mar tartalmazza a standard beallitasokat (Websocket, Proto).
        # Csak a specifikus javitas marad itt a Login loop ellen:
        proxy_set_header Authorization "";

        {{ if .proxy_pass_host }}
          # A $host változó használata ajánlottabb a $http_host helyett, 
          # mert kezeli az esetlegesen hiányzó kliens fejléceket is.
          proxy_set_header Host $host;
        {{ end }}
        {{ if .proxy_pass_real_ip }}
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header X-Real-IP $remote_addr;
        {{ end }}

        include /etc/nginx/includes/proxy_params.conf;
    }
}