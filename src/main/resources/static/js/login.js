(() => {
  const loginBtn = document.getElementById('loginBtn')
  const loginForm = document.getElementById('loginForm')
  loginBtn.addEventListener('click', () => {
    loginForm.submit()
  })
})()
