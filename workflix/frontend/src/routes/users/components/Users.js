// @flow

import React from 'react'
import { Card, H2, H3, Text } from '@blueprintjs/core'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import type { UserType } from '../../../modules/datatypes/User'
import UsersApi from '../../../modules/api/UsersApi'

type PropsType = {| users: Map<string, UserType>, path: string |}

class Users extends React.Component<PropsType> {
  render () {
    const users = Array.from(this.props.users.values())
    return <div>
      <H2 style={{ textAlign: 'center' }}>All Users</H2>
      {
        users.map(user => (
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

export default withPromiseResolver<PropsType, {| users: Map<string, UserType> |}>(promiseCreator)(Users)
