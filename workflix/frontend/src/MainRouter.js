// @flow

import React from 'react'
import { Redirect, Router } from '@reach/router'
import ProcessTemplates from './routes/process-templates/ProcessTemplates'
import CreateProcessTemplate from './routes/create-process-template/components/CreateProcessTemplate'
import TasksOverview from './routes/tasks/components/TasksOverview'
import Users from './routes/users/components/Users'

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
