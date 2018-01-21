(() => {

  const projectReaders = document.getElementById('projectReadersTable')
  const projectWriters = document.getElementById('projectWritersTable')
  const topicWriters = document.getElementById('topicWritersTable')
  const topicReaders = document.getElementById('topicReadersTable')
  const addPermProjectBtn = document.getElementById('addPermProjectBtn')
  const addPermTopicBtn = document.getElementById('addPermTopicBtn')
  let currentReaders = null
  let currentWriters = null

  if (projectReaders) {
    currentReaders = projectReaders
    projectReaders.addEventListener('click', function (ev) {
      const parent = ev.target.parentNode.parentNode
      if (parent.className === 'deleteReadPermission') {
        deletePermissionProject(parent.dataset.userId, 'READ')
      }
    })
  }

  if (projectWriters) {
    currentWriters = projectWriters
    projectWriters.addEventListener('click', function (ev) {
      const parent = ev.target.parentNode.parentNode
      if (parent.className === 'deleteWritePermission') {
        deletePermissionProject(parent.dataset.userId, 'WRITE')
      }
    })
  }

  if (addPermProjectBtn) {
    addPermProjectBtn.addEventListener('click', addPermissionProject)
  }

  if (topicWriters) {
    currentWriters = topicWriters
    topicWriters.addEventListener('click', function (ev) {
      const parent = ev.target.parentNode.parentNode
      if (parent.className === 'deleteWritePermission') {
        deletePermissionTopic(parent.dataset.userId, 'WRITE')
      }
    })
  }

  if (topicReaders) {
    currentReaders = topicReaders
    topicReaders.addEventListener('click', function (ev) {
      const parent = ev.target.parentNode.parentNode
      if (parent.className === 'deleteReadPermission') {
        deletePermissionTopic(parent.dataset.userId, 'READ')
      }
    })
  }

  if (addPermTopicBtn) {
    addPermTopicBtn.addEventListener('click', addPermissionTopic)
  }

  function addPermissionProject () {
    const projectId = document.getElementById('idProject').value

    const permSelector = document.getElementById('selectPermission')
    const permission = permSelector.options[permSelector.selectedIndex].value

    const userSelector = document.getElementById('selectUser')
    const userId = userSelector.options[userSelector.selectedIndex].value
    const username = userSelector.options[userSelector.selectedIndex].text

    axios.get(`/project/permission/${projectId}`, {
      params: {permission, userId}
    }).then(resp => {
      // We get back result of insertion false if already have perm
      const result = resp.data

      if (result['WRITE'] || result['READ']) {
        notie.alert(
          {type: 'info', text: `Permission ${permission} ajoutée`, time: 2})
      }

      if (result['WRITE']) {
        insertWriterNode(userId, username)
      }

      if (result['READ']) {
        insertReaderNode(userId, username)
      }
    })
  }

  function deletePermissionProject (userId, permission) {
    const projectId = document.getElementById('idProject').value
    axios.delete(`/project/permission/${projectId}`, {
      params: {permission, userId}
    }).then(resp => {
      // We get back result of insertion false if already removed perm
      const result = resp.data

      if (result['WRITE'] || result['READ']) {
        notie.alert(
          {type: 'info', text: `Permission ${permission} ajoutée`, time: 2})
      }

      if (result['WRITE']) {
        removeUserNode(userId, permission)
      }

      if (result['READ']) {
        removeUserNode(userId, permission)
      }
    })
  }

  function addPermissionTopic () {
    const topicId = document.getElementById('idTopic').value

    const permSelector = document.getElementById('selectPermission')
    const permission = permSelector.options[permSelector.selectedIndex].value

    const userSelector = document.getElementById('selectUser')
    const userId = userSelector.options[userSelector.selectedIndex].value
    const username = userSelector.options[userSelector.selectedIndex].text

    axios.get(`/topic/permission/${topicId}`, {
      params: {permission, userId}
    }).then(resp => {
      // We get back result of insertion false if already have perm
      const result = resp.data

      if (result['WRITE'] || result['READ']) {
        notie.alert(
          {type: 'info', text: `Permission ${permission} ajoutée`, time: 2})
      }

      if (result['WRITE']) {
        insertWriterNode(userId, username)
      }

      if (result['READ']) {
        insertReaderNode(userId, username)
      }
    })
  }

  function deletePermissionTopic (userId, permission) {
    const topicId = document.getElementById('idTopic').value
    axios.delete(`/topic/permission/${topicId}`, {
      params: {permission, userId}
    }).then(resp => {
      // We get back result of insertion false if already removed perm
      const result = resp.data

      if (result['WRITE'] || result['READ']) {
        notie.alert(
          {type: 'info', text: `Permission ${permission} ajoutée`, time: 2})
      }

      if (result['WRITE']) {
        removeUserNode(userId, permission)
      }

      if (result['READ']) {
        removeUserNode(userId, permission)
      }
    })
  }

  function insertReaderNode (id, text) {
    const row = currentReaders.insertRow(-1)

    const cell1 = row.insertCell(0)
    const cell2 = row.insertCell(1)

    cell1.innerText = text
    cell2.className = 'deleteReadPermission'
    cell2.innerHTML = '<i class="fas fa-trash has-text-danger is-clickable is-size-5"></i>'
    cell2.dataset.userId = id
    row.dataset.userId = id
  }

  function insertWriterNode (id, text) {
    const row = currentWriters.insertRow(-1)

    const cell1 = row.insertCell(0)
    const cell2 = row.insertCell(1)

    cell1.innerText = text
    cell2.className = 'deleteWritePermission'
    cell2.innerHTML = '<i class="fas fa-trash has-text-danger is-clickable is-size-5"></i>'
    cell2.dataset.userId = id
    row.dataset.userId = id
  }

  function removeUserNode (id, permission) {
    const rows = document.querySelectorAll(`tr[data-user-id='${id}']`)
    rows.forEach(row => {
      const table = row.closest('table')
      if (permission === 'WRITE' && table.id.endsWith('WritersTable')) {
        table.deleteRow(row.rowIndex)
      }
      if (permission === 'READ' && table.id.endsWith('ReadersTable')) {
        table.deleteRow(row.rowIndex)
      }
    })
  }
})()


