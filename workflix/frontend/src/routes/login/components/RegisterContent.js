// @flow

import React from 'react'
import { Button, Callout, FormGroup, InputGroup } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import handleStringChange from '../../../modules/common/handleStringChange'
import PasswordInput from './PasswordInput'

type PropsType = {|
  email: string,
  onGoToVerifyMail: () => void,
  onGoToLogin: () => void
|}

type StateType = {|
  code: string,
  password: string,
  password2: string,
  loading: boolean
|}

class RegisterContent extends React.Component<PropsType, StateType> {
  state = {
    code: '',
    password: '',
    password2: '',
    loading: false
  }

  codeInputRef = React.createRef<HTMLInputElement>()

  onFormSubmit = (e: Event) => {
    this.importAccount()
    e.preventDefault()
    return false
  }

  importAccount () {

  }

  isButtonDisabled = () => !this.state.password || !this.state.password2 || !this.state.code
  onPasswordChange = (password: string) => this.setState({ password })
  onPassword2Change = (password2: string) => this.setState({ password2 })
  onCodeChange = handleStringChange((code: string) => this.setState({ code }))

  render () {
    const { password, password2, code, loading } = this.state
    const { onGoToLogin, onGoToVerifyMail, email } = this.props
    return <form onSubmit={this.onFormSubmit} style={{ display: 'flex', flexDirection: 'column' }}>
      <Callout intent={Intent.PRIMARY} title='Import your QPlix account'
               style={{ maxWidth: '400px', wordWrap: 'break-word' }}>
        We sent a verification mail to <i>{email}</i> that contains a verification code. Enter this code below and
        choose a password to use with Workflix. You can then import your account.<br/>
        If you did not receive an email, check your spam folder or try the previous step again.
      </Callout>
      <FormGroup label='Verification code' labelFor='code' style={{ marginTop: '10px' }}>
        <InputGroup id='code' placeholder='Verification code' required large leftIcon='id-number'
                    onChange={this.onCodeChange} inputRef={this.codeInputRef} value={code}/>
      </FormGroup>
      <FormGroup label='Password' labelFor='password'>
        <PasswordInput password={password} onPasswordChange={this.onPasswordChange}/>
      </FormGroup>
      <FormGroup label='Repeat password' labelFor='password2'>
        <PasswordInput id='password2' placeholder='Repeat password' password={password2}
                       onPasswordChange={this.onPassword2Change}/>
      </FormGroup>
      <Button icon='new-person' intent={Intent.SUCCESS} text='Import account' type='submit' large
              disabled={this.isButtonDisabled()} loading={loading}/>
      <Button icon='fast-backward' text='Change email address' minimal style={{ marginTop: '5px' }}
              onClick={onGoToVerifyMail} disabled={loading}/>
      <Button icon='unlock' text='I already have an account' minimal style={{ marginTop: '5px' }}
              onClick={onGoToLogin} disabled={loading}/>
    </form>
  }

  componentDidMount () {
    if (this.codeInputRef.current) {
      this.codeInputRef.current.focus()
    }
  }
}

export default RegisterContent
