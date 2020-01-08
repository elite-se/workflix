// @flow

import React from 'react'
import { sortBy } from 'lodash'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import TitledCard from '../TitledCard'
import IconRow from '../IconRow'
import listIfNeeded from '../../listIfNeeded'
import { Button, ButtonGroup } from '@blueprintjs/core'

type PropsType = {|
  user: UserType,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>,
  onRoleSelected: (UserRoleType) => void,
  onProcessGroupSelected: (ProcessGroupType) => void,
  onSetEditing: (boolean) => void
|}

class UserCardRead extends React.Component<PropsType> {
  onProcessGroupSelected = (group: ProcessGroupType) => () => this.props.onProcessGroupSelected(group)
  onRoleSelected = (role: UserRoleType) => () => this.props.onRoleSelected(role)

  onEdit = () => this.props.onSetEditing(true)

  render () {
    const { user, processGroups, roles } = this.props
    const usersGroups = sortBy(user.processGroupIds.map(id => processGroups.get(id)).filter(Boolean),
      group => group.title)
    const usersRoles = sortBy(user.userRoleIds.map(id => roles.get(id)).filter(Boolean),
      role => role.name)
    return <TitledCard key={user.id} title={user.name}>
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
      <ButtonGroup fill style={{ marginTop: '5px' }}>
        <Button onClick={this.onEdit} icon='edit' small text='Edit'/>
      </ButtonGroup>
    </TitledCard>
  }
}

export default UserCardRead
