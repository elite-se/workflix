// @flow

import React from 'react'
import { Router } from '@reach/router'
import Users from './Users'
import ProcessTemplates from './ProcessTemplates'

const Home = () => <div>hi</div>

class MainRouter extends React.Component<{}> {
  render () {
    return <Router>
      <Home path='/' />
      <Users path='users' />
      <ProcessTemplates path='process-templates' />
    </Router>
  }
}

export default MainRouter
