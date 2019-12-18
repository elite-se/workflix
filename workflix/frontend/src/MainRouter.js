// @flow

import React from 'react'
import { Redirect, Router } from '@reach/router'
import Users from './components/Users'
import TasksOverview from './components/processes/TasksOverview'

class MainRouter extends React.Component<{}> {
  render () {
    return <Router>
      <Redirect from='/' to='tasks' />
      <TasksOverview path='tasks' />
      <Users path='users' />
    </Router>
  }
}

export default MainRouter
