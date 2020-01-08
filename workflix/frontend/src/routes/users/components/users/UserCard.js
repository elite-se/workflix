// @flow

import React from 'react'
import { sortBy } from 'lodash'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import TitledCard from '../TitledCard'
import IconRow from '../IconRow'
import listIfNeeded from '../../listIfNeeded'

type PropsType = {|
  user: UserType,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>,
  onRoleSelected: (UserRoleType) => void,
  onProcessGroupSelected: (ProcessGroupType) => void
|}

class UserCard extends React.Component<PropsType> {
  onProcessGroupSelected = (group: ProcessGroupType) => () => this.props.onProcessGroupSelected(group)
  onRoleSelected = (role: UserRoleType) => () => this.props.onRoleSelected(role)

  render () {
    const { user, processGroups, roles } = this.props
    const usersGroups = sortBy(user.processGroupIds.map(id => processGroups.get(id)).filter(Boolean),
      group => group.title)
    const usersRoles = sortBy(user.userRoleIds.map(id => roles.get(id)).filter(Boolean),
      role => role.name)
    return <TitledCard key={user.id} title={user.name}>
        <IconRow icon='person'>{user.displayname}</IconRow>
        <IconRow icon='envelope'><a href={`mailto:${user.email}`}>{user.email}</a></IconRow>
        <IconRow icon='office'>
          {listIfNeeded(usersGroups, group => group.id,
            group => <a onClick={this.onProcessGroupSelected(group)}>{group.title}</a>)}
        </IconRow>
        <IconRow icon='badge'>
          {listIfNeeded(usersRoles, role => role.id,
            role => <a onClick={this.onRoleSelected(role)}>{role.name}</a>)}
        </IconRow>
    </TitledCard>
  }
}

export default UserCard
