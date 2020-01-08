// @flow

import { Card, H3 } from '@blueprintjs/core'
import React from 'react'
import type { UserRoleType, UserType } from '../../../modules/datatypes/User'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import { sortBy } from 'lodash'
import IconRow from './IconRow'
import listIfNeeded from '../listIfNeeded'

type PropsType = {|
  user: UserType,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>
|}

class UserCard extends React.Component<PropsType> {
  render () {
    const { user, processGroups, roles } = this.props
    const usersGroups = sortBy(user.processGroupIds.map(id => processGroups.get(id)).filter(Boolean),
      group => group.title)
    const usersRoles = sortBy(user.userRoleIds.map(id => roles.get(id)).filter(Boolean),
      role => role.name)
    console.debug(user)
    return <Card key={user.id} style={{ margin: '5px' }}>
      <H3>{user.name}</H3>
        <IconRow icon='person'>{user.displayname}</IconRow>
        <IconRow icon='envelope'><a href={`mailto:${user.email}`}>{user.email}</a></IconRow>
        <IconRow icon='office'>
          {listIfNeeded(usersGroups, group => group.id, group => group.title)}
        </IconRow>
        <IconRow icon='badge'>
          {listIfNeeded(usersRoles, role => role.id, role => role.name)}
        </IconRow>
    </Card>
  }
}

export default UserCard
