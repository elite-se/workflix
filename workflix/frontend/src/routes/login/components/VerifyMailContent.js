// @flow

import React from 'react'
import { Button, Callout, FormGroup, InputGroup } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import handleStringChange from '../../../modules/common/handleStringChange'
import UsersApi from '../../../modules/api/UsersApi'
import { toastifyError } from '../../../modules/common/toastifyError'
import discardEvent from '../../../modules/common/discardEvent'

type PropsType = {|
  email: string,
  onEmailChange: (string) => void,
  onGoToLogin: () => void,
  onGoToRegister: () => void
|}

type StateType = {|
  loading: boolean
|}

class VerifyMailContent extends React.Component<PropsType, StateType> {
  state = { loading: false }
  emailInputRef = React.createRef<HTMLInputElement>()
  onEmailChange = handleStringChange(this.props.onEmailChange)

  onFormSubmit = (e: Event) => {
    this.onSendVerificationMail()
    e.preventDefault()
    return false
  }

  onSendVerificationMail = () => {
    this.setState({ loading: true })
    new UsersApi().sendVerificationMail(this.props.email)
      .then(() => {
        this.setState({ loading: false })
        this.props.onGoToRegister()
      })
      .catch(err => {
        this.setState({ loading: false })
        toastifyError(err)
      })
  }

  render () {
    const { email, onGoToLogin } = this.props
    const { loading } = this.state
    return <form style={{ display: 'flex', flexDirection: 'column' }} onSubmit={this.onFormSubmit}>
      <Callout intent={Intent.PRIMARY} title='Import your QPlix account'
               style={{ maxWidth: '400px', wordWrap: 'break-word' }}>
        Enter your email address.
        We will send you a verification email with a code that allows you to activate your account and set a password to
        log in to Workflix with.
      </Callout>
      <FormGroup label='Email' labelFor='email' style={{ marginTop: '10px' }}>
        <InputGroup id='email' placeholder='Email' required large leftIcon='person'
                    onChange={this.onEmailChange} inputRef={this.emailInputRef} value={email}
                    disabled={loading}/>
      </FormGroup>
      <Button icon='envelope' intent={Intent.PRIMARY} text='Verify email address' type='submit' large
              disabled={!email} loading={loading}/>
      <Button icon='unlock' text='I already have an account' minimal style={{ marginTop: '5px' }}
              onClick={discardEvent(onGoToLogin)} disabled={loading}/>
    </form>
  }

  componentDidMount () {
    if (this.emailInputRef.current) {
      this.emailInputRef.current.focus()
    }
  }
}

export default VerifyMailContent
