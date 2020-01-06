// @flow

import { Button, MenuItem } from '@blueprintjs/core'
import type { ItemPredicate } from '@blueprintjs/select'
import { ItemRenderer, Select } from '@blueprintjs/select'
import React from 'react'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import highlightText from '../../../modules/common/highlightText'

const CustomSelect = Select.ofType<ProcessGroupType>()

type PropsType = {
  items: ProcessGroupType[],
  activeItem: ?ProcessGroupType,
  onItemSelect: ProcessGroupType => void,
  intent?: string
}

class ProcessGroupSelect extends React.Component<PropsType> {
  itemRenderer: ItemRenderer = (item: ProcessGroupType, { handleClick, modifiers, query }) => {
    return <MenuItem
      active={modifiers.active}
      disabled={modifiers.disabled}
      icon={this.props.activeItem === item ? 'tick' : 'blank'}
      label={highlightText(item.title, query)}
      key={item.id}
      onClick={handleClick}
      shouldDismissPopover={false}
      text={highlightText(item.title, query)}/>
  }

  filter: ItemPredicate<ProcessGroupType> = (query, processGroup, _index, exactMatch) => {
    const normalizedName = processGroup.title.toLocaleLowerCase()
    const normalizedQuery = query.toLocaleLowerCase()
    return exactMatch
      ? [normalizedName].includes(normalizedQuery)
      : (normalizedName).indexOf(normalizedQuery) >= 0
  }

  render () {
    const { items, activeItem, onItemSelect, intent } = this.props
    return <CustomSelect items={items}
                         fill
                         popoverProps={{
                           usePortal: false,
                           fill: true
                         }}
                         activeItem={activeItem}
                         itemPredicate={this.filter}
                         itemRenderer={this.itemRenderer}
                         onItemSelect={onItemSelect}>
      <Button icon='people'
              rightIcon='caret-down'
              fill
              intent={intent}
              text={activeItem?.title || '(No selection)'}/>
    </CustomSelect>
  }
}

export default ProcessGroupSelect
