// @flow

import React from 'react'
import { Button } from '@blueprintjs/core'

class SortingHeader extends React.Component<{
  name: string,
  setSorting: (string, 'asc' | 'desc') => void,
  orderName: string,
  orderDir: 'asc' | 'desc',
  title: string
}> {
  onClick = () => {
    const { name, setSorting, orderName, orderDir } = this.props
    if (orderName === name) {
      setSorting(name, orderDir === 'asc' ? 'desc' : 'asc')
    } else {
      setSorting(name, 'asc')
    }
  }

  render () {
    const { name, title, orderName, orderDir } = this.props
    const rightIcon = orderName === name ? (orderDir === 'asc' ? 'sort-asc' : 'sort-desc') : null
    return <th><Button fill minimal onClick={this.onClick} rightIcon={rightIcon}>{title}</Button></th>
  }
}

export default SortingHeader
