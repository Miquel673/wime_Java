
function handleCredentialResponse(response) {
  const token = response.credential;

  fetch("/Wime/Controllers/GoogleDeviceController.php", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ token })
  })
  .then(res => res.json())
  .then(data => {
    if (data.success) {
      alert("✅ Bienvenido, " + data.nombre);
      // Puedes redirigir o guardar sesión
    } else {
      alert("❌ " + data.message);
    }
  })
  .catch(error => {
    console.error("Error al validar:", error);
  });
}

function handleCredentialResponse(response) {
  // Enviar token al backend
  fetch("/Wime/Controllers/GoogleDeviceController.php", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ token: response.credential })
  })
  .then(res => res.json())
  .then(data => {
    if (data.success) {
      window.location.href = "/Wime/private/PhP/Wime_interfaz_Tablero.php";
    } else {
      alert(data.message || "Error al iniciar sesión con Google");
    }
  })
  .catch(err => {
    console.error("Error:", err);
    alert("Hubo un problema con el inicio de sesión");
  });
}

function handleCredentialResponse(response) {
      console.log("ID Token: " + response.credential);
    }

    const msalConfig = {
      auth: {
        clientId: "TU_CLIENT_ID",
        authority: "https://login.microsoftonline.com/common",
        redirectUri: "https://tu-sitio.com/auth/microsoft/callback"
      }
    };
    const msalInstance = new msal.PublicClientApplication(msalConfig);

    function iniciarSesionMicrosoft() {
      msalInstance.loginPopup({ scopes: ["User.Read"] })
        .then(response => console.log("Usuario autenticado:", response.account))
        .catch(error => console.error("Error al autenticar:", error));
    }

    function checkLoginState() {
      FB.getLoginStatus(function (response) {
        if (response.status === 'connected') {
          console.log('Usuario autenticado:', response.authResponse);
        } else {
          console.log('Usuario no autenticado');
        }
      });
    }