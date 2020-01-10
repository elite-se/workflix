// @flow

import React from 'react'
import { Card, H1 } from '@blueprintjs/core'
import CenterScreen from '../../../modules/common/components/CenterScreen'
import LoginContent from './LoginContent'
import VerifyMailContent from './VerifyMailContent'
import RegisterContent from './RegisterContent'

type ModeType = 'login' | 'verifyMail' | 'register'

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
  onGoToVerifyMail = () => this.setState({ mode: 'verifyMail' })
  onGoToRegister = () => this.setState({ mode: 'register' })
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
      case 'verifyMail':
        return <VerifyMailContent email={email} onEmailChange={this.onEmailChange} onGoToLogin={this.onGoToLogin}
        onGoToRegister={this.onGoToRegister}/>
      case 'register': return <RegisterContent email={email} onGoToLogin={this.onGoToLogin}
                                               onGoToVerifyMail={this.onGoToVerifyMail}/>
      default:
        return <LoginContent email={email} onEmailChange={this.onEmailChange} onLoggedIn={this.onLoggedIn}
                             onGoToVerifyMail={this.onGoToVerifyMail}/>
    }
  }
}

export default Login
