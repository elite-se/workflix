// @flow

import React from 'react'
import { Button, FormGroup, InputGroup } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import LoginApi from '../../../modules/api/LoginApi'
import { storeToken } from '../../../modules/common/tokenStorage'
import { toastifyError } from '../../../modules/common/toastifyError'
import handleStringChange from '../../../modules/common/handleStringChange'
import PasswordInput from './PasswordInput'

type PropsType = {|
  email: string,
  onEmailChange: (string) => void,
  onLoggedIn: () => void,
  onGoToVerifyMail: () => void
|}

type StateType = {|
  password: string,
  loading: boolean
|}

class LoginContent extends React.Component<PropsType, StateType> {
  state = {
    password: '',
    loading: false
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
  onPasswordChange = (password: string) => this.setState({ password })

  render () {
    const { password, loading } = this.state
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
        <PasswordInput password={password} onPasswordChange={this.onPasswordChange}/>
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
