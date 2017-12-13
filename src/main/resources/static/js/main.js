(() => {
  const changers = document.querySelectorAll('.lang-changer')

  for (changer of changers) {
    changer.addEventListener('click', function (ev) {
      const lang = this.dataset.lang
      window.location.href = this.href + '?lang=' + lang
    })
  }
})()
