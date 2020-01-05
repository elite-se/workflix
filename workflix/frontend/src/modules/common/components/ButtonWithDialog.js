// @flow

import React from 'react'
import { Alert, Button, IButtonProps } from '@blueprintjs/core'

class ButtonWithDialog extends React.Component<IButtonProps, { alertOpen: boolean }> {
  state = { alertOpen: false }

  onClickButton = () => this.setState({ alertOpen: true })

  onClickConfirm = () => this.props.onClick()

  onClose = () => this.setState({ alertOpen: false })

  render () {
    const { alertOpen } = this.state
    const { icon, intent, text, children } = this.props
    return <>
      <Button {...this.props} onClick={this.onClickButton} children={null}/>
      <Alert isOpen={alertOpen} icon={icon} intent={intent} confirmButtonText={text} onConfirm={this.onClickConfirm}
             cancelButtonText='Cancel' onClose={this.onClose}>
        {children}
      </Alert>
    </>
  }
}

export default ButtonWithDialog
