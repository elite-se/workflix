// @flow

import React from 'react'
import { Button, Callout, FormGroup, InputGroup } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import handleStringChange from '../../../modules/common/handleStringChange'
import PasswordInput from './PasswordInput'
import { toastifyError } from '../../../modules/common/toastifyError'
import UsersApi from '../../../modules/api/UsersApi'
import discardEvent from '../../../modules/common/discardEvent'

type PropsType = {|
  email: string,
  onGoToVerifyMail: () => void,
  onGoToLogin: (showSuccess?: boolean) => void
|}

type StateType = {|
  code: string,
  password: string,
  password2: string,
  name: string,
  displayName: string,
  loading: boolean
|}

class RegisterContent extends React.Component<PropsType, StateType> {
  state = {
    code: '',
    password: '',
    password2: '',
    name: '',
    displayName: '',
    loading: false
  }

  codeInputRef = React.createRef<HTMLInputElement>()

  onFormSubmit = (e: Event) => {
    this.importAccount()
    e.preventDefault()
    return false
  }

  importAccount () {
    const { password, code, name, displayName } = this.state
    const { email, onGoToLogin } = this.props
    this.setState({ loading: true })
    new UsersApi().createUser(email, password, code, name, displayName)
      .then(() => {
        this.setState({ loading: false })
        onGoToLogin(true)
      })
      .catch(err => {
        this.setState({ loading: false })
        toastifyError(err)
      })
  }

  isSubmitDisabled = () => !this.state.password || !this.state.password2 || !this.state.code ||
    this.state.password !== this.state.password2 || !this.state.name || !this.state.displayName

  onPasswordChange = (password: string) => this.setState({ password })
  onPassword2Change = (password2: string) => this.setState({ password2 })
  onNameChange = handleStringChange((name: string) => {
    // update displayName with first letters of each word of name
    console.debug(name)
    const displayName = name.split(' ').map(word => word.substring(0, 1)).join('')
    this.setState({ name, displayName })
  })

  onDisplayNameChange = handleStringChange((displayName: string) => this.setState({ displayName }))
  onCodeChange = handleStringChange((code: string) => this.setState({ code }))

  render () {
    const { password, password2, code, name, displayName, loading } = this.state
    const { onGoToLogin, onGoToVerifyMail, email } = this.props
    const passwordsMatch = password === password2
    return <form onSubmit={this.onFormSubmit} style={{ display: 'flex', flexDirection: 'column' }}>
      <Callout intent={Intent.PRIMARY} title='Create your account account'
               style={{ maxWidth: '400px', wordWrap: 'break-word' }}>
        We sent a verification mail to <i>{email}</i> that contains a verification code. Enter this code and your name below and
        choose a password to use with Workflix. You can then import your account.<br/>
        If you did not receive an email, check your spam folder or try the previous step again.
      </Callout>
      <FormGroup label='Verification code' labelFor='code' style={{ marginTop: '10px' }}>
        <InputGroup id='code' placeholder='Verification code' required large leftIcon='id-number'
                    onChange={this.onCodeChange} inputRef={this.codeInputRef} value={code}/>
      </FormGroup>
      <FormGroup label='Name' labelFor='name'>
        <InputGroup id='name' placeholder='Your name' required large leftIcon='person' onChange={this.onNameChange} value={name}/>
      </FormGroup>
      <FormGroup label='Initials' labelFor='displayName'>
        <InputGroup id='displayName' placeholder='Your initials' required large leftIcon='tag' onChange={this.onDisplayNameChange} value={displayName}/>
      </FormGroup>
      <FormGroup label='Password' labelFor='password'>
        <PasswordInput id='password' password={password} onPasswordChange={this.onPasswordChange}/>
      </FormGroup>
      <FormGroup label='Repeat password' labelFor='password2'
                 helperText={passwordsMatch ? undefined : 'Passwords must not differ'}>
        <PasswordInput id='password2' placeholder='Repeat password' password={password2}
                       onPasswordChange={this.onPassword2Change} intent={passwordsMatch ? undefined : Intent.DANGER}/>
      </FormGroup>
      <Button icon='new-person' intent={Intent.SUCCESS} text='Create account' type='submit' large
              disabled={this.isSubmitDisabled()} loading={loading}/>
      <Button icon='fast-backward' text='Change email address' minimal style={{ marginTop: '5px' }}
              onClick={onGoToVerifyMail} disabled={loading}/>
      <Button icon='unlock' text='I already have an account' minimal style={{ marginTop: '5px' }}
              onClick={discardEvent(onGoToLogin)} disabled={loading}/>
    </form>
  }

  componentDidMount () {
    if (this.codeInputRef.current) {
      this.codeInputRef.current.focus()
    }
  }
}

export default RegisterContent
