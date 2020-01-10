// @flow

import React from 'react'
import { Callout, Card, H1 } from '@blueprintjs/core'
import CenterScreen from '../../../modules/common/components/CenterScreen'
import LoginContent from './LoginContent'
import VerifyMailContent from './VerifyMailContent'
import RegisterContent from './RegisterContent'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'

type ModeType = 'login' | 'verifyMail' | 'register'

type StateType = {
  email: string,
  mode: ModeType,
  showRegisteredMessage: boolean
}

class Login extends React.Component<{ onLoggedInChanged: (boolean) => void }, StateType> {
  state = {
    email: '',
    mode: 'login',
    showRegisteredMessage: false
  }

  onEmailChange = (email: string) => this.setState({ email })
  onLoggedIn = () => this.props.onLoggedInChanged(true)
  onGoToVerifyMail = () => this.setState({ mode: 'verifyMail', showRegisteredMessage: false })
  onGoToRegister = () => this.setState({ mode: 'register', showRegisteredMessage: false })
  onGoToLogin = (showRegisteredMessage?: boolean = false) => this.setState({ mode: 'login', showRegisteredMessage })

  render () {
    return <CenterScreen>
      <Card>
        <H1 style={{ marginBottom: '20px' }}>Welcome to Workflix!</H1>
        {this.state.showRegisteredMessage &&
        <Callout intent={Intent.SUCCESS} title='Your account was imported'
                 style={{ maxWidth: '400px', wordWrap: 'break-word', marginBottom: '10px' }}>
          You can now log in using the email and password you just specified.
        </Callout>}
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
