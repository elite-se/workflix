// @flow

import React from 'react'
import { Card, H1 } from '@blueprintjs/core'
import CenterScreen from '../../../modules/common/components/CenterScreen'
import LoginContent from './LoginContent'
import RegistrationContent from './RegistrationContent'

type ModeType = 'login' | 'verifyEmail' | 'register'

type StateType = {
  email: string,
  mode: ModeType
}

class Login extends React.Component<{ onLoggedInChanged: (boolean) => void }, StateType> {
  state = {
    email: '',
    mode: 'login'
  }

  onEmailChange = (email: string) => this.setState({ email })
  onLoggedIn = () => this.props.onLoggedInChanged(true)
  onGoToRegister = () => this.setState({ mode: 'verifyEmail' })
  onGoToLogin = () => this.setState({ mode: 'login' })

  render () {
    return <CenterScreen>
      <Card>
        <H1 style={{ marginBottom: '20px' }}>Welcome to Workflix!</H1>
        {this.renderContent()}
      </Card>
    </CenterScreen>
  }

  renderContent = () => {
    const { email, mode } = this.state
    switch (mode) {
      case 'verifyEmail':
        return <RegistrationContent email={email} onEmailChange={this.onEmailChange} onGoToLogin={this.onGoToLogin}/>
      default:
        return <LoginContent email={email} onEmailChange={this.onEmailChange} onLoggedIn={this.onLoggedIn}
                             onGoToRegister={this.onGoToRegister}/>
    }
  }
}

export default Login
