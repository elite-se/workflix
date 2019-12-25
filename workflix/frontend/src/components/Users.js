// @flow

import React from 'react'
import { Card, H2, H3, Text } from '@blueprintjs/core'
import withPromiseResolver from './withPromiseResolver'
import type { UserType } from '../datatypes/models'
import UsersApi from '../api/UsersApi'

type PropsType = {| users: Array<UserType>, path: string |}

class Users extends React.Component<PropsType> {
  render () {
    return <div>
      <H2 style={{ textAlign: 'center' }}>All Users</H2>
      {
        this.props.users.map(user => (
          <Card key={user.id} style={{ margin: '5px' }}>
            <H3>{user.displayname}</H3>
            <Text>{user.name}</Text>
            <a href={`mailto:${user.email}`}>{user.email}</a>
          </Card>)
        )
      }
    </div>
  }
}

const promiseCreator = () => new UsersApi().getUsers().then(users => ({ users }))

export default withPromiseResolver<PropsType, {| users: Array<UserType> |}>(promiseCreator)(Users)
