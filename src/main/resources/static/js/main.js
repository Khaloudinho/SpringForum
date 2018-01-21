(() => {
  const token = document.querySelector('meta[name=\'_csrf\']').getAttribute(
    'content')
  const header = document.querySelector(
    'meta[name=\'_csrf_header\']').getAttribute('content')

  window.axios = axios.create({
    baseURL: 'http://localhost:8080/',
    headers: {[header]: token}
  })

  const enChanger = document.querySelector('.en-lang-changer')
  const frChanger = document.querySelector('.fr-lang-changer')
  const logoutBtn = document.getElementById('logoutBtn')
  const logoutForm = document.getElementById('logoutForm')

  if (window.location.href.includes('?lang=en')) {
    enChanger.style.display = 'none'
  } else {
    frChanger.style.display = 'none'
  }

  enChanger && enChanger.addEventListener('click', function () {
    window.location.href = this.href + '?lang=en'
    enChanger.style.display = 'none'
    frChanger.style.display = 'flex'
  })

  frChanger && frChanger.addEventListener('click', function () {
    window.location.href = this.href + '?lang=fr'
    frChanger.style.display = 'none'
    enChanger.style.display = 'flex'
  })

  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
      logoutForm.submit()
    })
  }
})()
