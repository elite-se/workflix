// @flow

import React from 'react'
import styled from 'styled-components'
import { Button } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'

const Container = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex-flow: row wrap;
  justify-content: center;
  align-items: fill;
  align-content: flex-start;
  max-width: 100%;
`

class CardsContainer extends React.Component<{ onCreate?: () => void, children: React$Node }> {
  render () {
    return <div style={{ maxWidth: '100%' }}>
      {this.props.onCreate && <Button icon='add' text='Create new' style={{ alignSelf: 'flex-start', margin: '5px' }}
                                      intent={Intent.SUCCESS} onClick={this.props.onCreate}/>}
      <Container>{this.props.children}</Container>
    </div>
  }
}

export default CardsContainer
