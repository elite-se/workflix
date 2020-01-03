// @flow

import React from 'react'
import { Redirect, Router } from '@reach/router'
import Users from './components/Users'
import TasksOverview from './components/processes/TasksOverview'
import ProcessTemplates from './components/ProcessTemplates'
import CreateProcessTemplate from './routes/create-process-template/CreateProcessTemplate'

class MainRouter extends React.Component<{}> {
  render () {
    return <Router>
      <Redirect from='/' to='tasks'/>
      <TasksOverview path='tasks'/>
      <Users path='users'/>
      <ProcessTemplates path='process-templates'/>
      <CreateProcessTemplate path='process-templates/create'/>
    </Router>
  }
}

export default MainRouter
