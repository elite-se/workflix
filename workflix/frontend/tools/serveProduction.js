const express = require('express')
const path = require('path')
const server = express()

/* route requests for static files */
server.use('/', express.static(path.join(__dirname, '/../build')))

/* catch-all route to index.html defined last */
server.get('/*', (req, res) => {
  res.sendFile(path.join(__dirname, '/../build/index.html'))
})

// eslint-disable-next-line no-magic-numbers
const port = process.env.PORT || 8080
server.listen(port, function () {
  console.log(`server listening on port ${port}`)
})
