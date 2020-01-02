// @flow

import React from 'react'
import { Router } from '@reach/router'
import Users from './Users'
import ProcessTemplates from './ProcessTemplates'
import CreateProcessTemplate from './CreateProcessTemplate'

const Home = () => <div>hi</div>

class MainRouter extends React.Component<{}> {
  render () {
    return <Router>
      <Home path='/' />
      <Users path='users' />
      <ProcessTemplates path='process-templates' />
      <CreateProcessTemplate path='process-templates/create' />
    </Router>
  }
}

export default MainRouter
