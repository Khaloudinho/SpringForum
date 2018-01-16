(() => {
  const changers = document.querySelectorAll('.lang-changer')
  const logoutBtn = document.getElementById('logoutBtn')
  const logoutForm = document.getElementById('logoutForm')

  for (changer of changers) {
    changer.addEventListener('click', function () {
      const lang = this.dataset.lang
      window.location.href = this.href + '?lang=' + lang
    })
  }

  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
      logoutForm.submit()
    })
  }
})()
