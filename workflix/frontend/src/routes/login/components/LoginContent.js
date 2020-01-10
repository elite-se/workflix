// @flow

import React from 'react'
import { Button, FormGroup, InputGroup, Tooltip } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import LoginApi from '../../../modules/api/LoginApi'
import { storeToken } from '../../../modules/common/tokenStorage'
import { toastifyError } from '../../../modules/common/toastifyError'
import handleStringChange from '../../../modules/common/handleStringChange'

type PropsType = {|
  email: string,
  onEmailChange: (string) => void,
  onLoggedIn: () => void,
  onGoToVerifyMail: () => void
|}

type StateType = {|
  password: string,
  loading: boolean,
  showPassword: boolean
|}

class LoginContent extends React.Component<PropsType, StateType> {
  state = {
    password: '',
    loading: false,
    showPassword: false
  }

  emailInputRef = React.createRef<HTMLInputElement>()

  onFormSubmit = (e: Event) => {
    this.login()
    e.preventDefault()
    return false
  }

  login () {
    const { password } = this.state
    const { email, onLoggedIn } = this.props
    this.setState({ loading: true })
    new LoginApi().login(email, password)
      .then(token => {
        if (token) {
          storeToken(token)
          this.setState({ loading: false })
          onLoggedIn()
        }
      })
      .catch(err => {
        this.setState({ loading: false })
        toastifyError(err, {
          icon: 'key'
        })
      })
  }

  onEmailChange = handleStringChange(this.props.onEmailChange)

  isButtonDisabled = () => !this.props.email || !this.state.password
  onPasswordChange = handleStringChange(password => this.setState({ password }))

  toggleShowPassword = () => {
    this.setState(oldState => ({ showPassword: !oldState.showPassword }))
  }

  render () {
    const { showPassword, password, loading } = this.state
    return <form
      onSubmit={this.onFormSubmit}
      style={{
        display: 'flex',
        flexDirection: 'column'
      }}>
      <FormGroup label='Email' labelFor='email'>
        <InputGroup id='email' placeholder='Email' required large leftIcon='person'
                    onChange={this.onEmailChange} inputRef={this.emailInputRef} value={this.props.email}/>
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
              disabled={this.isButtonDisabled()} loading={loading}/>
      <Button icon='new-person' text='Import my ***REMOVED*** account' minimal style={{ marginTop: '5px' }}
              onClick={this.props.onGoToVerifyMail} disabled={loading}/>
    </form>
  }

  componentDidMount () {
    if (this.emailInputRef.current) {
      this.emailInputRef.current.focus()
    }
  }
}

export default LoginContent
