// @flow

import React from 'react'
import { CenterScreen } from '../../../modules/common/centerScreen'
import { Button, Card, FormGroup, H1, InputGroup, Spinner, Tooltip } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import handleStringChange from '../../../modules/common/handleStringChange'
import AppToaster from '../../../modules/app/AppToaster'
import LoginApi from '../../../modules/api/LoginApi'
import { storeToken } from '../../../modules/common/tokenStorage'
import { navigate } from '@reach/router'

class Login extends React.Component<{}, { email: string, password: string, loading: boolean, showPassword: boolean }> {
  state = {
    email: '',
    password: '',
    loading: false,
    showPassword: false
  }

  emailInputRef = React.createRef<HTMLInputElement>()

  isButtonDisabled = () => this.state.loading || !this.state.email || !this.state.password
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
    new LoginApi().getToken(email, password)
      .then(token => {
        storeToken(token)
        navigate('/')
      })
      .catch(err => {
        AppToaster.show({
          message: err.message,
          intent: Intent.DANGER,
          icon: 'key'
        })
      })
      .finally(() => this.setState({ loading: false }))
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
          {this.state.loading
            ? <Spinner/>
            : <Button icon='unlock' intent={Intent.SUCCESS} text='Login' type='submit' large
                      disabled={this.isButtonDisabled()}/>}
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
