// @flow

import React from 'react'
import { IMultiSelectProps, ItemListPredicate, ItemRenderer, MultiSelect } from '@blueprintjs/select'
import { Button, MenuItem } from '@blueprintjs/core'
import { filter, isEmpty, sortBy } from 'lodash'
import highlightText from '../highlightText'

type PropsType<T, IdType: number | string> = {|
  items: T[],
  selection: T[],
  onItemAdded: (T) => void,
  onItemRemoved: (T) => void,
  onItemsCleared: () => void,
  render: (T) => string,
  toID: (T) => IdType,
  multiSelectProps?: IMultiSelectProps
|}

class SimpleMultiSelect<T, IdType: number | string> extends React.Component<PropsType<T, IdType>> {
  renderItem: ItemRenderer<T> = (item, { modifiers, handleClick, query }) => {
    if (!modifiers.matchesPredicate) {
      return null
    }
    return <MenuItem
      active={modifiers.active}
      icon={this.isSelected(item) ? 'tick' : 'blank'}
      key={this.props.toID(item)}
      onClick={handleClick}
      text={highlightText(this.props.render(item), query)}
      shouldDismissPopover={false}
    />
  }

  isSelected: (T => boolean) = item => this.props.selection.includes(item)

  onItemSelect = (item: T) =>
    this.isSelected(item)
      ? this.props.onItemRemoved(item)
      : this.props.onItemAdded(item)

  onTagRemove = (tag: string, index: number) => {
    const item = this.props.selection[index]
    item && this.props.onItemRemoved(item)
  }

  itemListPredicate: ItemListPredicate<T> = (query, items) => {
    const queryLower = query.toLocaleLowerCase()
    const render = this.props.render
    return sortBy(filter(items, item => render(item).toLocaleLowerCase().includes(queryLower)),
      item => !render(item).toLocaleLowerCase().startsWith(queryLower),
      item => render(item).toLocaleLowerCase()
    )
  }

  render () {
    const MultiSelectInstance = MultiSelect.ofType<T>()
    const clearButton = !isEmpty(this.props.selection) &&
      <Button icon='cross' minimal onClick={this.props.onItemsCleared}/>
    return <MultiSelectInstance
      {...this.props.multiSelectProps}
      itemListPredicate={this.itemListPredicate}
      itemRenderer={this.renderItem}
      items={this.props.items}
      noResults={<MenuItem icon='disable' text='No results.' disabled/>}
      onItemSelect={this.onItemSelect}
      tagRenderer={this.props.render}
      tagInputProps={{ onRemove: this.onTagRemove, rightElement: clearButton }}
      selectedItems={this.props.selection}
      resetOnSelect/>
  }
}

export default SimpleMultiSelect
