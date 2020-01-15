// @flow

import React from 'react'
import { sortBy } from 'lodash'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import StyledCard from '../StyledCard'
import IconRow from '../IconRow'
import listIfNeeded from '../../listIfNeeded'
import stopPropagation from '../../../../modules/common/stopPropagation'
import { H3 } from '@blueprintjs/core'

type PropsType = {|
  user: UserType,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>,
  onUserSelected: (UserType) => void,
  onRoleSelected: (UserRoleType) => void,
  onProcessGroupSelected: (ProcessGroupType) => void
|}

class UserCardRead extends React.Component<PropsType> {
  onProcessGroupSelected = (group: ProcessGroupType) => stopPropagation(() => this.props.onProcessGroupSelected(group))
  onRoleSelected = (role: UserRoleType) => stopPropagation(() => this.props.onRoleSelected(role))
  onClick = () => this.props.onUserSelected(this.props.user)

  render () {
    const { user, processGroups, roles } = this.props
    const usersGroups = sortBy(user.processGroupIds.map(id => processGroups.get(id)).filter(Boolean),
      group => group.title.toLocaleLowerCase())
    const usersRoles = sortBy(user.userRoleIds.map(id => roles.get(id)).filter(Boolean),
      role => role.name.toLocaleLowerCase())
    return <StyledCard key={user.id} onClick={this.onClick} interactive>
      <H3>{user.name}</H3>
      <IconRow icon='person'>{user.displayname}</IconRow>
      <IconRow icon='envelope'><a href={`mailto:${user.email}`}>{user.email}</a></IconRow>
      <IconRow icon='office' multiLine>
        {listIfNeeded(usersGroups, group => group.id,
          group => <a onClick={this.onProcessGroupSelected(group)}>{group.title}</a>)}
      </IconRow>
      <IconRow icon='badge' multiLine>
        {listIfNeeded(usersRoles, role => role.id,
          role => <a onClick={this.onRoleSelected(role)}>{role.name}</a>)}
      </IconRow>
    </StyledCard>
  }
}

export default UserCardRead
