(function() {
    try {
      // Leer preferencia (si no existe, usar 'claro' o usar prefers-color-scheme como fallback)
      var tema = localStorage.getItem('tema');
      if (!tema) {
        // fallback dinámico por sistema (opcional)
        if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
          tema = 'oscuro';
        } else {
          tema = 'claro';
        }
      }

      // Aplicar en <html> para mayor consistencia (puedes cambiar por document.body)
      if (tema === 'oscuro') {
        document.documentElement.classList.add('dark-mode');
      } else {
        document.documentElement.classList.remove('dark-mode');
      }

      // Exponer para debugging (opcional)
      window.__WIME_TEMA = tema;
    } catch (e) {
      console.error('Error aplicando tema:', e);
    }
  })();