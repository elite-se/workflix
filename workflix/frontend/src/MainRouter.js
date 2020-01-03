// @flow

import React from 'react'
import { Redirect, Router } from '@reach/router'
import Users from './routes/users/Users'
import ProcessTemplates from './components/ProcessTemplates'
import CreateProcessTemplate from './routes/create-process-template/CreateProcessTemplate'
import TasksOverview from './routes/tasks/TasksOverview'

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
