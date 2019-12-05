// @flow

import React from 'react'
import { Router } from '@reach/router'
import Users from './Users'

const Home = () => <div>hi</div>

class MainRouter extends React.Component<{}> {
  render () {
    return <Router>
      <Home path='/' />
      <Users path='users' />
    </Router>
  }
}

export default MainRouter
