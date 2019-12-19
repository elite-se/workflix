// @flow

import React from 'react'
import { Card, H2, H3, Text } from '@blueprintjs/core'
import type { UserType } from './models'
import withPromiseResolver from './withPromiseResolver'

type PropsType = {| users: Array<UserType>, path: string |}

class Users extends React.Component<PropsType> {
  render () {
    return <div style={{
      margin: '20px',
      display: 'flex',
      maxWidth: '300px',
      flex: '1',
      justifyContent: 'flex-start',
      alignItems: 'stretch',
      flexDirection: 'column'
    }}>
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

const promiseCreator = () => fetch('https://wf-backend.herokuapp.com/users')
  .then(response => response.json())
  .then(users => ({ users }))

export default withPromiseResolver<PropsType, {| users: Array<UserType> |}>(promiseCreator)(Users)
