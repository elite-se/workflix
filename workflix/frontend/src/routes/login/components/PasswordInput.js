// @flow

import React from 'react'
import { Button, IInputGroupProps, InputGroup, Tooltip } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import handleStringChange from '../../../modules/common/handleStringChange'

type PropsType = {
  password: string,
  onPasswordChange: (string) => void
}

type StateType = {|
  showPassword: boolean
|}

class PasswordInput extends React.Component<PropsType & IInputGroupProps, StateType> {
  state = { showPassword: false }

  toggleShowPassword = () => {
    this.setState(oldState => ({ showPassword: !oldState.showPassword }))
  }

  onPasswordChange = handleStringChange(this.props.onPasswordChange)

  render () {
    const { password, onPasswordChange, ...inputProps } = this.props
    const { showPassword } = this.state
    return <InputGroup id='password' placeholder='Password' required large
                       type={showPassword ? 'text' : 'password'}
                       onChange={this.onPasswordChange} value={password}
                       leftIcon='key'
                       rightElement={<Tooltip content={`${showPassword ? 'Hide' : 'Show'} password`}>
                         <Button
                           tabIndex='-1'
                           minimal large
                           icon={showPassword ? 'eye-open' : 'eye-off'}
                           intent={Intent.WARNING}
                           onClick={this.toggleShowPassword}
                         />
                       </Tooltip>}
                       {...inputProps}/>
  }
}

export default PasswordInput
