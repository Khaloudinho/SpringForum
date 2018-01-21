(() => {
  const loginBtn = document.getElementById('loginBtn')
  const loginForm = document.getElementById('loginForm')
  const loginBtn2 = document.getElementById('loginBtn2')
  const loginForm2 = document.getElementById('loginForm2')
  loginBtn.addEventListener('click', () => {
    loginForm.submit()
  })

  loginBtn2.addEventListener('click', () => {
    loginForm2.submit()
  })

  // loginForm.addEventListener('keypress', (ev) => {
  //   const key = ev.key
  //   console.log(key)
  // })
})()
