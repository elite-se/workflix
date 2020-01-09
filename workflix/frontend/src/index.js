// @flow

import React from 'react'
import ReactDOM from 'react-dom'
import '@blueprintjs/core/lib/css/blueprint.css'
import '@blueprintjs/icons/lib/css/blueprint-icons.css'
import '@blueprintjs/datetime/lib/css/blueprint-datetime.css'
import '@blueprintjs/select/lib/css/blueprint-select.css'

import App from './App'
import * as serviceWorker from './serviceWorker'

const root = document.getElementById('root')
const splash = document.getElementById('splash')
if (!root || !splash) {
  throw new Error('The app went nuts.')
}

ReactDOM.render(<App/>, root)

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister()
splash.className = 'remove'
