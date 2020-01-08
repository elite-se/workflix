// @flow

import React from 'react'
import { Card, H3, ICardProps } from '@blueprintjs/core'

class TitledCard extends React.Component<{ cardProps?: ICardProps, title: React$Node, children: React$Node }> {
  render () {
    return <Card interactive style={{ margin: '5px' }} {...this.props.cardProps}>
      <H3>{this.props.title}</H3>
      {this.props.children}
    </Card>
  }
}

export default TitledCard
