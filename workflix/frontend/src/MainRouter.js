// @flow

import React from 'react'
import { Redirect, Router } from '@reach/router'
import Users from './components/Users'
import ProcessOverview from './components/processes/ProcessOverview'

class MainRouter extends React.Component<{}> {
  render () {
    return <Router>
      <Redirect from='/' to='processes' />
      <ProcessOverview path='processes' />
      <Users path='users' />
    </Router>
  }
}

export default MainRouter
