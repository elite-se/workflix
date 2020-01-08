// @flow

import React from 'react'
import { Card, H3, ICardProps } from '@blueprintjs/core'

class TitledCard extends React.Component<ICardProps & { title: React$Node, children: React$Node }> {
  render () {
    const { title, children, ...cardProps } = this.props
    return <Card interactive style={{ margin: '5px', width: '320px' }} {...cardProps}>
      <H3>{title}</H3>
      {children}
    </Card>
  }
}

export default TitledCard
