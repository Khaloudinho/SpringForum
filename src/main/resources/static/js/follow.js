(() => {
  const followBtn = document.getElementById('followBtn')
  const unfollowBtn = document.getElementById('unfollowBtn')

  followBtn && followBtn.addEventListener('click', follow)
  unfollowBtn && unfollowBtn.addEventListener('click', unfollow)

  function follow () {
    const topicId = document.getElementById('idTopic').value

    axios.get(`/topic/${topicId}/follow`).then(resp => {
      followBtn.style.display = 'none'
      unfollowBtn.style.display = 'inline-flex'
    })
  }

  function unfollow () {
    const topicId = document.getElementById('idTopic').value

    axios.get(`/topic/${topicId}/unfollow`).then(resp => {
      followBtn.style.display = 'inline-flex'
      unfollowBtn.style.display = 'none'
    })
  }
})()

