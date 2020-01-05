// @flow

import React from 'react'
import { CenterScreen } from '../../../modules/common/centerScreen'
import { Button, Card, FormGroup, InputGroup } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'

class Login extends React.Component<{}, { email: string, password: string }> {
  state = {
    email: '',
    password: ''
  }

  login () {
    alert('Not yet implemented')
  }

  render () {
    return <CenterScreen>
      <Card style={{
          display: 'flex',
          flexDirection: 'column'
        }}>
          <FormGroup
            label='Email'
            labelFor='email'>
            <InputGroup id='email' placeholder='Email'/>
          </FormGroup>
          <FormGroup
            label='Password'
            labelFor='password'>
            <InputGroup id='password' placeholder='Password' type='password'/>
          </FormGroup>
          <Button icon='unlock' intent={Intent.SUCCESS} text='Login' onClick={this.login} type='submit'/>
      </Card>
    </CenterScreen>
  }
}

export default Login
