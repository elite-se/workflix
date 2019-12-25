// @flow

import React from 'react'
import type { UserType } from '../datatypes/models'
import UsersApi from '../api/UsersApi'

type PropsType = {
  userId: string
}

class LoadingUsername extends React.Component<PropsType, { user: ?UserType }> {
  state = {}

  componentDidMount () {
    new UsersApi().getUser(this.props.userId)
      .then(user => this.setState({ user: user || null }))
      .catch(error => {
        console.error(error)
        this.setState({ user: null })
      })
  }

  render () {
    return this.state.user === undefined
      ? <span className='bp3-skeleton'>{this.props.userId}</span>
      : this.state.user === null
        ? this.props.userId
        : this.state.user.name
  }
}

export default LoadingUsername
