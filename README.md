# Componente de envío de correo por medio de Spring Boot y Microsoft Graph


## Configuración en Azure Portal

```text
┌─────────────────────────────────────────────────────────────────┐
│                    Azure Active Directory                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. Registrar aplicación:                                        │
│     Azure Portal → Azure AD → App registrations → New            │
│                                                                  │
│  2. Obtener credenciales:                                        │
│     • Tenant ID     → Overview                                   │
│     • Client ID     → Overview                                   │
│     • Client Secret → Certificates & secrets → New secret        │
│                                                                  │
│  3. Configurar permisos API:                                     │
│     API permissions → Add permission → Microsoft Graph           │
│                                                                  │
│     ┌──────────────────────────────────────────────────────┐    │
│     │  Permiso requerido (Application):                     │    │
│     │  ✓ Mail.Send                                          │    │
│     │                                                        │    │
│     │  Opcional:                                             │    │
│     │  ✓ Mail.ReadWrite (para guardar borradores)           │    │
│     │  ✓ User.Read.All (para validar usuarios)              │    │
│     └──────────────────────────────────────────────────────┘    │
│                                                                  │
│  4. Grant admin consent (requiere admin)                         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## ⚠️ Notas Importantes

Aspecto	Detalle

Licencia=	El usuario remitente necesita licencia Exchange Online

Permisos=	Mail.Send como permiso de aplicación (no delegado)

Admin Consent=	Un administrador debe aprobar los permisos

Límites=	Microsoft limita a ~10,000 correos/día por buzón


## Configuración de variables de entorno

A nivel de desarrollo, copia el archivo .env.example y cámbiale el nombre a .env
Luego reemplaza los valores por los verdaderos.

## Enviar correo simple:

```bash
curl -X POST http://localhost:8080/api/email/send \
-H "Content-Type: application/json" \
-d '{
"to": ["destinatario@ejemplo.com"],
"cc": ["copia@ejemplo.com"],
"subject": "Prueba componente de correo",
"body": "<h1>Hola!</h1><p>Este es un correo de prueba.</p>",
"isHtml": true,
"importance": "high"
}'
```


## Enviar correo con adjunto:

```bash
curl -X POST http://localhost:8080/api/email/send-with-attachment \
  -F 'email={"to":["destinatario@ejemplo.com"],"subject":"Con adjunto","body":"Ver archivo adjunto","isHtml":false};type=application/json' \
  -F 'file=@/ruta/al/archivo.pdf'
```