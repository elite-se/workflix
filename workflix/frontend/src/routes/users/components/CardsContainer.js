// @flow

import React from 'react'
import styled from 'styled-components'
import { Button } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import FlipMove from 'react-flip-move'

const Container = styled(FlipMove)`
  display: flex;
  flex-flow: row wrap;
  justify-content: center;
  align-content: flex-start;
  max-width: 100%;
`

type PropsType = {|
  onCreate?: () => Promise<*>,
  children: React$Node
|}

class CardsContainer extends React.Component<PropsType, {| creating: boolean |}> {
  state = { creating: false }

  onCreate = () => {
    const onCreate = this.props.onCreate
    if (!onCreate) { return }
    this.setState({ creating: true })
    onCreate().finally(() => this.setState({ creating: false }))
  }

  render () {
    return <div style={{ maxWidth: '100%', display: 'flex', flexDirection: 'column' }}>
      {this.props.onCreate &&
      <Button icon='plus' text='Create new' style={{ alignSelf: 'flex-end', margin: '5px' }}
              intent={Intent.SUCCESS} onClick={this.onCreate} loading={this.state.creating}/>}
      <Container appearAnimation='fade'>{this.props.children}</Container>
    </div>
  }
}

export default CardsContainer
