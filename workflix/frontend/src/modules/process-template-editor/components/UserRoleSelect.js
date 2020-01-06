// @flow

import { Button, MenuItem } from '@blueprintjs/core'
import type { UserRoleType } from '../../datatypes/User'
import type { ItemPredicate } from '@blueprintjs/select'
import { ItemRenderer, Select } from '@blueprintjs/select'
import highlightText from '../../common/highlightText'
import React from 'react'

const CustomSelect = Select.ofType<UserRoleType>()

type PropsType = {
  userRoles: UserRoleType[],
  activeItem: ?UserRoleType,
  onItemSelect: UserRoleType => void,
  intent?: string
}

class UserRoleSelect extends React.Component<PropsType> {
  itemRenderer: ItemRenderer = (item: UserRoleType, { handleClick, modifiers, query }) => {
    return <MenuItem
      active={modifiers.active}
      disabled={modifiers.disabled}
      icon={this.props.activeItem === item ? 'tick' : 'blank'}
      label={highlightText(item.name, query)}
      key={item.id}
      onClick={handleClick}
      shouldDismissPopover={false}
      text={highlightText(item.name, query)}/>
  }

  filterUsers: ItemPredicate<UserRoleType> = (query, user, _index, exactMatch) => {
    const normalizedName = user.name.toLocaleLowerCase()
    const normalizedQuery = query.toLocaleLowerCase()
    return exactMatch
      ? [normalizedName].includes(normalizedQuery)
      : (normalizedName).indexOf(normalizedQuery) >= 0
  }

  render () {
    const { userRoles, activeItem, onItemSelect, intent } = this.props
    return <CustomSelect items={userRoles}
                         fill
                         popoverProps={{
                           usePortal: false,
                           fill: true
                         }}
                         itemPredicate={this.filterUsers}
                         itemRenderer={this.itemRenderer}
                         onItemSelect={onItemSelect}>
      <Button icon='people'
              rightIcon='caret-down'
              fill
              intent={intent}
              text={activeItem?.name || '(No selection)'}/>
    </CustomSelect>
  }
}

export default UserRoleSelect
