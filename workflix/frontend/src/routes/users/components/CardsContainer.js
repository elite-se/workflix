// @flow

import React from 'react'
import styled from 'styled-components'
import { H2 } from '@blueprintjs/core'

const Container = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex-flow: row wrap;
  justify-content: center;
  align-items: flex-start;
  align-content: flex-start;
  max-width: 100%;
`

class CardsContainer extends React.Component<{ title: React$Node, children: React$Node }> {
  render () {
    return <div style={{ maxWidth: '100%' }}>
      <H2 style={{ textAlign: 'center' }}>{this.props.title}</H2>
      <Container>{this.props.children}</Container>
    </div>
  }
}

export default CardsContainer
