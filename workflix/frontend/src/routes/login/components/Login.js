// @flow

import React from 'react'
import { Button, Card, FormGroup, H1, InputGroup, Tooltip } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import handleStringChange from '../../../modules/common/handleStringChange'
import LoginApi from '../../../modules/api/LoginApi'
import { storeToken } from '../../../modules/common/tokenStorage'
import CenterScreen from '../../../modules/common/components/CenterScreen'
import { toastifyError } from '../../../modules/common/toastifyError'

type StateType = {
  email: string,
  password: string,
  loading: boolean,
  showPassword: boolean
}

class Login extends React.Component<{ onLoggedInChanged: (boolean) => void}, StateType> {
  state = {
    email: '',
    password: '',
    loading: false,
    showPassword: false
  }

  emailInputRef = React.createRef<HTMLInputElement>()

  isButtonDisabled = () => !this.state.email || !this.state.password
  onEmailChange = handleStringChange(email => this.setState({ email }))
  onPasswordChange = handleStringChange(password => this.setState({ password }))

  onFormSubmit = (e: Event) => {
    this.login()
    e.preventDefault()
    return false
  }

  login () {
    const { email, password } = this.state
    this.setState({ loading: true })
    new LoginApi().login(email, password)
      .then(token => {
        if (token) {
          storeToken(token)
          this.setState({ loading: false })
          this.props.onLoggedInChanged(true)
        }
      })
      .catch(err => {
        this.setState({ loading: false })
        toastifyError(err, {
          icon: 'key'
        })
      })
  }

  toggleShowPassword = () => {
    this.setState(oldState => ({ showPassword: !oldState.showPassword }))
  }

  render () {
    const { email, password, showPassword } = this.state
    return <CenterScreen>
      <Card>
        <H1 style={{ marginBottom: '20px' }}>Welcome to Workflix!</H1>
        <form
          onSubmit={this.onFormSubmit}
          style={{
            display: 'flex',
            flexDirection: 'column'
          }}>
          <FormGroup label='Email' labelFor='email'>
            <InputGroup id='email' placeholder='Email' required large leftIcon='person'
                        onChange={this.onEmailChange} inputRef={this.emailInputRef} value={email}/>
          </FormGroup>
          <FormGroup label='Password' labelFor='password'>
            <InputGroup id='password' placeholder='Password' required large
                        type={showPassword ? 'text' : 'password'}
                        onChange={this.onPasswordChange} value={password}
                        leftIcon='key'
                        rightElement={<Tooltip content={`${showPassword ? 'Hide' : 'Show'} password`}><Button
                          minimal large
                          icon={showPassword ? 'eye-open' : 'eye-off'}
                          intent={Intent.WARNING}
                          onClick={this.toggleShowPassword}
                        /></Tooltip>}/>
          </FormGroup>
          <Button icon='unlock' intent={Intent.SUCCESS} text='Login' type='submit' large
                      disabled={this.isButtonDisabled()} loading={this.state.loading}/>
        </form>
      </Card>
    </CenterScreen>
  }

  componentDidMount () {
    if (this.emailInputRef.current) {
      this.emailInputRef.current.focus()
    }
  }
}

export default Login