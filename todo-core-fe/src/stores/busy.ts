import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import {v4 as uuidv4} from 'uuid';

export const useBusyStore = defineStore('busy', () => {
    const loadingActions = ref(new Set<string>());
    const blockingActions = ref(new Set<string>());
    const isAtLeastLoading = computed(() => isLoading.value || isBlocking.value);
    const isLoading = computed(() => loadingActions.value.size > 0);
    const isBlocking = computed(() => blockingActions.value.size > 0);
    const addLoading = (): string => {
        const uuid = uuidv4();
        loadingActions.value.add(uuid);
        return uuid;
    }
    const addBlocking = (): string => {
        const uuid = uuidv4();
        blockingActions.value.add(uuid);
        return uuid;
    }
    const removeLoading = (uuid: string): void => {
        loadingActions.value.delete(uuid);
    }
    const removeBlocking = (uuid: string): void => {
        blockingActions.value.delete(uuid);
    }
    return { loadingActions, blockingActions, isAtLeastLoading, isLoading, isBlocking, addLoading, addBlocking, removeLoading, removeBlocking };
});
