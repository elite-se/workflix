// @flow

import React from 'react'
import { Button, Callout, FormGroup, InputGroup } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import handleStringChange from '../../../modules/common/handleStringChange'

type PropsType = {|
  email: string,
  onEmailChange: (string) => void,
  onGoToLogin: () => void
|}

type StateType = {|
  loading: boolean
|}

class RegistrationContent extends React.Component<PropsType, StateType> {
  state = { loading: false }

  emailInputRef = React.createRef<HTMLInputElement>()

  onEmailChange = handleStringChange(this.props.onEmailChange)

  render () {
    const { email, onGoToLogin } = this.props
    const { loading } = this.state
    return <form
      style={{
        display: 'flex',
        flexDirection: 'column'
      }}>
      <Callout intent={Intent.PRIMARY} title='Import your QPlix account'
               style={{ maxWidth: '400px', wordWrap: 'break-word' }}>
        Enter the email address you are registered with in the ***REMOVED*** system.
        We will send you a verification email with a code that allows you to import your account and set a password to
        log in to Workflix with.
      </Callout>
      <FormGroup label='Email' labelFor='email' style={{ marginTop: '10px' }}>
        <InputGroup id='email' placeholder='Email' required large leftIcon='person'
                    onChange={this.onEmailChange} inputRef={this.emailInputRef} value={email}/>
      </FormGroup>
      <Button icon='envelope' intent={Intent.PRIMARY} text='Verify email address' type='submit' large
              disabled={!email} loading={loading}/>
      <Button icon='unlock' text='I already have an account' minimal style={{ marginTop: '5px' }}
              onClick={onGoToLogin} disabled={loading}/>
    </form>
  }

  componentDidMount () {
    if (this.emailInputRef.current) {
      this.emailInputRef.current.focus()
    }
  }
}

export default RegistrationContent
