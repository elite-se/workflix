// @flow

import React from 'react'
import { Card, H1 } from '@blueprintjs/core'
import CenterScreen from '../../../modules/common/components/CenterScreen'
import LoginContent from './LoginContent'

type StateType = {
  email: string
}

class Login extends React.Component<{ onLoggedInChanged: (boolean) => void}, StateType> {
  state = {
    email: ''
  }

  onEmailChange = (email: string) => this.setState({ email })
  onLoggedIn = () => this.props.onLoggedInChanged(true)

  render () {
    const { email } = this.state
    return <CenterScreen>
      <Card>
        <H1 style={{ marginBottom: '20px' }}>Welcome to Workflix!</H1>
        <LoginContent email={email} onEmailChange={this.onEmailChange} onLoggedIn={this.onLoggedIn}/>
      </Card>
    </CenterScreen>
  }
}

export default Login
